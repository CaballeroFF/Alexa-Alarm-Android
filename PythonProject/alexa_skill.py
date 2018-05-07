from flask import Flask, request
from flask_ask import Ask, statement, question
from datetime import datetime

import delighted_clock as dl
import multiprocessing
import psutil
import os
import json

app = Flask(__name__)
ask = Ask(app, "/skill")

dct = {}


@app.route('/')
def homepage():
	return "hi there, how is it going."


@app.route('/deletealarm', methods=['POST', 'GET'])
def delete_alarm_route():
	send_message = 'nothing sent'

	if request.method == 'POST':
		# get data from POST method (get_data() will be deprecated soon)
		# post_data is assumed to be a key from dct
		post_data = request.get_data().decode()
		# post_data is 'hh:mm yyyy-MM-dd' format
		date_list = str(post_data).split(" ")
		# time portion of data after split
		time = date_list[0]
		# date portion of data after split
		date = date_list[1]
		# delete alarm
		no_intent(time)
		send_message = 'alarm {} {} deleted'.format(time, date)
	return json.dumps(send_message)


@app.route('/clearalarms', methods=['GET'])
def clear_alarms_route():
	send_message = 'nothing sent'

	if request.method == 'GET':
		# clear alarms on method GET
		clear_alarms()
		send_message = 'alarms cleared'

	return json.dumps(send_message)


@app.route('/listalarms', methods=['GET'])
def alarms_list_route():
	send_message = 'nothing sent'

	if request.method == 'GET':
		# list alarms on method GET
		list_alarms()
		# string of all dct keys (the alarms) joined by a space
		send_message = ' '.join(list(dct.keys()))
	return json.dumps(send_message)


@app.route('/setalarm', methods=['GET', 'POST'])
def alarm_set_route():
	send_message = 'nothing sent'
	is_duration = False

	if request.method == 'POST':
		# get data from POST method (get_data() will be deprecated soon)
		post_data = request.get_data().decode()
		# post_data is 'hh:mm yyyy-MM-dd' format
		date_list = str(post_data).split(" ")
		# time portion of data after split
		time = date_list[0]
		# date portion of data after split
		date = date_list[1]

		# check if time is in ISO-8601 duration format
		# meaning it has a letter not just numbers
		for character in time:
			if character.isalpha():
				is_duration = True

		# if the time is a duration call duration function
		# otherwise call normal set alarm function
		if is_duration:
			duration(time)
		else:
			yes_intent(time, date)
		send_message = 'alarm set for {} {}'.format(time, date)

		# returns message when GET method is called
		return json.dumps(send_message)


def print_console(msg):
	# text Border
	pounds = '#'
	# + 7 because msg has 8 extra characters(#...msg...#)
	# and pounds already has one #
	for i in range(len(msg) + 7):
		pounds += '#'
	# print top and bottom borders and msg in middle
	print('\n' + pounds)
	print('#   ' + msg + '   #')
	print(pounds + '\n')


@ask.launch
def satart_skill():
	welcome_msg = 'Hello there, can I help you?'
	print_console(welcome_msg)
	return question(welcome_msg)


@ask.intent("Duration", default={'aduration': 'default'})
def duration(aduration):
	global dct
	print('Intent duration result: ', aduration)

	atime = dl.alarm_duration(aduration)[0]
	adate = dl.alarm_duration(aduration)[1]
	print('the date is', atime, ' ', adate)

	p = multiprocessing.Process(target=set_alarm, args=(atime, adate))
	p.start()
	n = p.pid
	print('created child process: ', multiprocessing.active_children())
	print('with process ID: ', n)

	dct[atime] = n
	print('Dictionary entries: ', dct)

	d = datetime.strptime(atime, '%H:%M')
	print('formated time: ', d.strftime("%I:%M %p"))

	msg = 'your alarm is set to {}, {}'.format(d.strftime("%I:%M %p"), adate)
	print_console(msg)
	return statement(msg)


@ask.intent("ClearIntent")
def clear_alarms():
	global dct

	update_dct()

	if dct:
		for k in dct:
			p = psutil.Process(dct[k])
			p.terminate()
		msg = 'clearing alarms.'
		dct = {}
	else:
		msg = 'alarms already empty.'
	print_console(msg)
	return statement(msg)


@ask.intent("ListIntent")
def list_alarms():
	global dct

	update_dct()
	if len(dct) > 1:
		alarm = 'alarms are '
	else:
		alarm = 'alarm is '
	msg = 'your ' + alarm
	if dct:
		print('list is not empty')
		for i, k in enumerate(dct):
			separator = ' , '
			if i == len(dct) - 1:
				separator = '.'
			if i == len(dct) - 2:
				separator = ' and '
			d = datetime.strptime(k, '%H:%M')
			msg = msg + d.strftime("%I:%M %p") + separator
	else:
		print('list is empty')
		msg = msg + 'empty'
	print_console(msg)
	return statement(msg)


@ask.intent("YesIntent", default={'atime': '9:20', 'adate': 'today'})
def yes_intent(atime, adate):
	global dct
	print('the date is', atime, ' ', adate)

	p = multiprocessing.Process(target=set_alarm, args=(atime, adate), name='noname')
	p.start()
	n = p.pid
	print('created child process: ', multiprocessing.active_children())
	print('with process ID: ', n)
	dct[atime] = n

	print('dictionary ............', dct)
	print('............', dct[atime])

	d = datetime.strptime(atime, '%H:%M')
	print('formated time "', d.strftime("%I:%M %p"))
	headline_msg = 'your alarm is set to {}, {}'.format(d.strftime("%I:%M %p"), adate)
	print_console(headline_msg)

	return statement(headline_msg)


@ask.intent("NoIntent", default={"atime": None})
def no_intent(atime):
	global dct

	update_dct()
	bye_msg = ''

	analarm = False

	print('\nactive child process: ', multiprocessing.active_children())
	parent = psutil.Process(os.getpid())

	for key in dct:
		print(key)
		if key == atime:
			print(key, True)
			analarm = True

	for child in parent.children(recursive=True):
		print('child...... ', child)
	if not dct or atime is None:
		print('ok........')
		print('Dictionary ...........', dct)
		bye_msg = 'alarms are empty.'
	elif not analarm:
		d = datetime.strptime(atime, '%H:%M')
		bye_msg = '{} is not in your alarms'.format(d.strftime("%I:%M %p"))
	else:
		# print(dct, dct[atime])
		p = psutil.Process(dct[atime])
		p.terminate()
		dct.pop(atime, None)
		d = datetime.strptime(atime, '%H:%M')
		bye_msg = '{} alarm deleted'.format(d.strftime("%I:%M %p"))
	for child in parent.children(recursive=True):
		print('child.....', child)

		print_console(bye_msg)
	return statement(bye_msg)


@ask.intent("DefaultIntent")
def default_state():
	update_dct()
	msg = 'I\'m sorry, I didn\'t understand that'
	print_console(msg)
	return statement(msg)


def set_alarm(tstr, dstr):
	global dct

	returned = dl.set_alarm(tstr, dstr)
	print(returned, ' ,its back')


def update_dct():
	global dct
	print('dictionary.......', dct)
	zombies = []
	for k in dct:
		print('pid: ', dct[k])
		p = psutil.Process(dct[k])
		print(p.status())
		if p.status() == psutil.STATUS_ZOMBIE:
			print('.........zombie state..........')
			zombies.append(k)
	for dead in zombies:
		del dct[dead]


if __name__ == "__main__":
	app.run(debug=True)
