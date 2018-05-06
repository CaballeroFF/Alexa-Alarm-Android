import RPi.GPIO as GPIO
from time import sleep
from pygame import mixer

redpin = 12							# PWM pin connected to LED
greenpin = 13
ir = 36
btn = 40

pr_pwm = None
pg_pwm = None


def build():
	global pr_pwm, pg_pwm
	global redpin, greenpin, ir
	
	mixer.init()
	mixer.music.load('alarm.mp3')

	GPIO.setwarnings(False)				#disable warnings
	GPIO.setmode(GPIO.BOARD)			#set pin numbering system
	GPIO.setup(redpin,GPIO.OUT)
	GPIO.setup(greenpin,GPIO.OUT)
	GPIO.setup(ir, GPIO.IN)
	GPIO.setup(btn, GPIO.IN)

	pr_pwm = GPIO.PWM(redpin,1000)
	pg_pwm = GPIO.PWM(greenpin,1000)		#create PWM instance with frequency
	pr_pwm.start(0)							#start PWM of required Duty Cycle 
	pg_pwm.start(0)


def ir_approach():
	global ir
	global pr_pwm, pg_pwm
	
	pr_pwm.ChangeDutyCycle(100)	
	pg_pwm.ChangeDutyCycle(100)
	return GPIO.input(ir)
	

def btn_press():
	global btn
	global pr_pwm, pg_pwm
	
	pr_pwm.ChangeDutyCycle(100)	
	pg_pwm.ChangeDutyCycle(100)
	return GPIO.input(btn)
	


def start(SLEEP=0.05, MSTART=100):
	global pr_pwm, pg_pwm
	
	try:
		for duty in range(100,-1,-1):
			green_dcycle = (.3030 * (duty)) + 70 
			if(green_dcycle>100):
				green_dcycle=100
			pr_pwm.ChangeDutyCycle(duty)	
			pg_pwm.ChangeDutyCycle(int(green_dcycle))
		#	pg_pwm.ChangeDutyCycle(duty)
			print(green_dcycle,'1')
			#print('[0--o---o--0]') 
			if duty <= MSTART:
				mixer.music.play(start=0)
				mixer.music.rewind()
			sleep(SLEEP)
			if duty <= MSTART:
				mixer.music.stop()
		sleep(0.001)

		pr_pwm.ChangeDutyCycle(0)	
		pg_pwm.ChangeDutyCycle(70) 
		 
		'''    for duty in range(100,-1,-1):
				green_dcycle = (.3030 * duty) + 70
				if(green_dcycle>100):
					green_dcycle=100
				pr_pwm.ChangeDutyCycle(duty)
				pg_pwm.ChangeDutyCycle(int(green_dcycle))
		#		pg_pwm.ChangeDutyCycle(duty)
				print(green_dcycle,'2')
				sleep(0.1)
			sleep(0.5)
		'''

	except KeyboardInterrupt:
		GPIO.cleanup()

def one_time():
	global pr_pwm, pg_pwm
	
	try:
		#while flag:
		mixer.music.play(start=0)
		mixer.music.rewind()
		#print('[0--o---o--0]')
		pr_pwm.ChangeDutyCycle(0)	
		pg_pwm.ChangeDutyCycle(70)  
		sleep(1)
		mixer.music.stop()
	except KeyboardInterrupt:
		GPIO.cleanup()


def clean_up():
	mixer.music.stop()
	GPIO.cleanup()
	
#start()
#one_time()
#clean_up()
#ir_approach()
