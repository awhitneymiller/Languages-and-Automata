# Homework 6
*Audrey Whitney-Miller*

## What is the difference between concurrency and parallelism? 
Concurrency is when an application(or something) has two or more tasks, starting, running and completing in the same time period. While it's dealing with multiple tasks at once, its not necessarily executing them simultaneosly. It's like multitasking, like a single server serving multiple customers by switching between them as fast as they can. 

Parallelism is when mutliple tasks are running/executing  at the same time, on multiple processing units. Like multiple servers serving multiple customers at the same time. 

## What is the difference between a thread and a task in Java? Give an example.
Tasks are units of work that a thread performs. There can be a thread for each task to be performed, or there can a single thread performing each task one after the other, or there can be a bunch of threads that run a task and get the available task to perform. For example, a thread is like a worker, while a task is just a task given to the worker to do. 

## What happens if you invoke a method on a thread that has terminated in Java? If you call an entry on a task that has terminated in Ada?
 In Java, if you invoke a method on a thread that has been terminated, the method will execute normally because the Thread object usually remains in memory and behaves like any other object. In Ada, calling an extra on a task that has been terminated with taise a Tasking_Error because Ada executes concurrently.

## Explain, for Ada, Java, and Go, when, exactly, a program terminates. That is, will it terminate when the main thread finishes, or will it wait for certain other threads to finish first?
**Ada**: The main program (environment task(main program thread)) waits for all the other dependent tasks have terminated. 
**Java**: Java only terminates automatically when the main thread finishes and all non-daemon threads have finished. If any non-daemon threads are still running, the JVM keeps running until they complete. Daemon threads are ignored at shutdown and do not block termination.
**Go**: The program terminates when the main goroutine returns.

## In Go, what is the difference between a buffered and unbuffered channel? Provide an example of when you would use each.
**Unbuffered channel**:An unbuffered channel makes the sender and receiver wait for each other, so communication happens only when both are ready. You use it when you need strict synchronization, like signaling that a worker is done.

**Buffered channel**:A buffered channel lets the sender add items without waiting until the buffer is full. You use it when producers may run faster than consumers and you want to smooth out bursts of work.

## Explain the difference between a mutex and a read-write mutex (RWMutex) in Go. When would you choose one over the other?
A mutex lets only one goroutine access a critical section at any time, whether it’s reading or writing, while an RWMutex allows many readers to access shared data at once but only one writer, blocking all readers during a write. You would use a mutex when reads and writes are frequent and shared access doesn’t help, and an RWMutex when reads heavily outnumber writes and you want multiple goroutines to read safely in parallel.

## What happens if you try to read from or write to a closed channel in Go? How can you detect if a channel is closed?
If you read from a closed channel, you get the remaining buffered values and then zero values after it’s empty. If you write to a closed channel, your program panics. You can detect a closed channel with the “comma ok” form `(v, ok := <-ch)`, where `ok` is `false` if the channel is closed.

## Describe the select statement in Go and how it differs from a switch statement. What happens when multiple channels in a select are ready simultaneously?
The `select` statement lets a goroutine wait on multiple channel operations and runs whichever case becomes ready first, while a switch just matches values and doesn’t involve channels. If multiple channels in a select are ready at the same time then Go picks one case at random to keep things fair.