#defining the RPi's pins as Input / Output
import RPi.GPIO as GPIO

#importing the library for delaying command.
import time 


#closing the warnings when you are compiling the code
#GPIO.setwarnings(False)


#defining the pins
green = 20
red = 21
blue = 22

#choosing a frequency for pwm
#Freq = 100
#SLEEP = .2
#RUNNING = True


def pwm_led(Freq=100, SLEEP=.004, RUNNING=True):
	#used for GPIO numbering
	GPIO.setmode(GPIO.BCM) 

	#defining the pins as output
	GPIO.setup(red, GPIO.OUT) 
	GPIO.setup(green, GPIO.OUT)
	GPIO.setup(blue, GPIO.OUT)

	#defining the pins that are going to be used with PWM
	RED = GPIO.PWM(red, Freq)  
	GREEN = GPIO.PWM(green, Freq)
	BLUE = GPIO.PWM(blue, Freq)

	try:
		#we are starting with the loop
		while RUNNING: 
			#lighting up the pins. 1 means giving 100% to the pin
			RED.start(100) #0
			GREEN.start(100) #70
			BLUE.start(100)
			
			for i in range(100):
				green_dcycle = (.3030 * (100 - i)) + 70
				if green_dcycle >= 100:
					green_dcycle = 100
				RED.ChangeDutyCycle(100 - i)
				GREEN.ChangeDutyCycle(green_dcycle)
				time.sleep(SLEEP)
				#print('loop 1', green_dcycle, ' ', i)
			
			for i in range(100):
				green_dcycle = (.3030 * i) + 70
				if green_dcycle >= 100:
					green_dcycle = 100
				RED.ChangeDutyCycle(i)
				GREEN.ChangeDutyCycle(green_dcycle)
				time.sleep(SLEEP)
				#print('loop 2', green_dcycle, ' ', i)
			RUNNING = False
			GPIO.cleanup()
			

	except KeyboardInterrupt: 
		# the purpose of this part is, when you interrupt the code, it will stop the while loop and turn off the pins, which means your LED won't light anymore
		RUNNING = False  
		GPIO.cleanup()


#pwm_led(SLEEP=.005)
