hello 
first code : 

when we call thread.start the run method will be Run and the thread will be sleep for 10 second 

after that thread will interrupt and interruptExeption will be catch and proggram write : "Thread was interrupted!"
and after that the finally block will be run and proggram print : "Thread will be finished here!!!" 

second code :     

​	in this code Thread.currentThread().getName() is main and when we run this code proggram will print :  "Running in: main "
but if we use DirectRunnable insted task the proggram will print : "Running in: nameofthread  "



third : 

when we start this thread.start(); will be run run method and print : "Running in: thread_0 " and mainthread will be stopped with thread.join until thread_0 finished and after that proggram will print : "Back to: main "