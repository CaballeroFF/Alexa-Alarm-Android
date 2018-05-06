import sys
import time
import datetime
from pygame import mixer
import delight_leds as dl
import threading


def parse_duration(dstr):
    time_type = ''
    t_index = dstr.index('T')
    for i, elem in enumerate(dstr):
        if elem != 'P' and elem != 'T':
            if elem.isnumeric():
                time_type += elem
            if elem.isalpha() and len(dstr) - 1 != i:
                if elem == 'M' and i >= t_index:
                    time_type += elem.lower() + ' '
                else:
                    time_type += elem + ' '
            if len(dstr) - 1 == i:
                if elem == 'M' and i >= t_index:
                    time_type += elem.lower()
                else:
                    time_type += elem
    duration = time_type.split(' ')
    return duration


def duration_to_minute(dstr):
    dlist = parse_duration(dstr)
    year = 0
    month = 0
    week = 0
    day = 0
    hour = 0
    minute = 0
    for i, elem in enumerate(dlist):
        if 'Y' in elem:
            year = int(elem[:-1]) * 525600
        if 'M' in elem:
            month = int(elem[:-1]) * 43800
        if 'W' in elem:
            week = int(elem[:-1]) * 10078
        if 'D' in elem:
            day = int(elem[:-1]) * 1440
        if 'H' in elem:
            hour = int(elem[:-1]) * 60
        if 'm' in elem:
            minute = int(elem[:-1])
    minutes = year + month + week + day + hour + minute
    return minutes


def alarm_duration(aduration):
    ctime = time.localtime()
    dt = datetime.datetime.fromtimestamp(time.mktime(ctime))
    c = dt + datetime.timedelta(minutes=duration_to_minute(aduration))
    adate = str(c.date())
    atime = str(c.hour) + ':' + str(c.minute)

    #set_alarm(atime, adate)
    return atime, adate


#sa = set alarm //// sd = set date
def set_alarm(salarm, sdate):
	mixer.init()
	mixer.music.load('alarm.mp3')

	ctime = time.localtime()

	idate = sdate
	if sdate == 'today':
		idate = str(ctime.tm_year) + '-' + str(ctime.tm_mon) + '-' + str(ctime.tm_mday)
	print('default', idate)

	itime = salarm
	timelist = itime.split(':')
	datelist = idate.split('-')

	hour = abs(int(timelist[0]) - ctime.tm_hour)
	print('hours ', hour)

	minute = int(timelist[1]) - ctime.tm_min
	print('minute(s)', minute)

	time_in_min = (hour * 60) + minute
	print('total time in minutes', time_in_min)

	try:
		minutes = time_in_min
	except ValueError:
		print("Invalid numeric value (%s) for minutes" % salarm)
		print("Should be an integer >= 0")
		sys.exit(1)

	if minutes < 0:
		print("Invalid value for minutes, should be >= 0")
		sys.exit(1)

	change = minutes

	if minutes == 1:
		unit_word = " minute"
	else:
		unit_word = " minutes"

	try:
		print("Sleeping for " + str(minutes) + unit_word)
		while not (ctime.tm_hour == int(timelist[0]) and ctime.tm_min == int(timelist[1]) and ctime.tm_mday == int(datelist[2])):
			if change != minutes:
				change = minutes
				print("Sleeping for " + str(minutes) + unit_word)
				print(ctime.tm_hour, ctime.tm_min)
			time.sleep(1)

			ctime = time.localtime()
			hour = abs(int(timelist[0]) - ctime.tm_hour)
			minute = int(timelist[1]) - ctime.tm_min
			time_in_min = (hour * 60) + minute
			minutes = time_in_min
			if minutes < 0:
				minutes = minutes + 1440
		
		print("Wake up")
		dl.build()
		#SLEEP controls the speed of the leds
		#MSTART indicates when the beeping starts
		dl.start(SLEEP= 0.1, MSTART= 25)
		print('in loop')
		
		stop = False
		while dl.ir_approach():
			print(chr(7))
			dl.one_time()
			time.sleep(.01)
			#print('btn is ', dl.btn_press())
			stop = dl.btn_press()
			if stop:
				break
		dl.clean_up()
		
		if not stop:
			snooze_min = int(timelist[1]) + 1 #snooze time
			snooze_hr = int(timelist[0])
			if snooze_min > 59:
				snooze_min -= 59 
				snooze_hr += 1
			if snooze_hr > 23:
				snooze_hr -= 24
			print(salarm)
			print(str(snooze_hr) + ':' + str(snooze_min))
			new_time = str(snooze_hr) + ':' + str(snooze_min)
			set_alarm(new_time, sdate)
		print('stopped')
		
		
			
	except KeyboardInterrupt:
		print("Interrupted by user")
		mixer.music.stop()
		dl.clean_up()
		sys.exit(1)

	return -1
# EOF
#set_alarm('15:03','2018-03-12')

#debug
#set_alarm(str(time.localtime().tm_hour) + ':' + str(time.localtime().tm_min),'2018-03-17')
#print(alarm_duration(u'PT3M'))
