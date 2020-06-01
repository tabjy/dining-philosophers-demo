package com.tabjy.dining_philosophers;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiningPhilosophers implements Runnable{

	public static int NUM_OF_PHILOSOPHERS = 5;

	public static void main(String[] args) throws Exception {
		Scanner scnr = new Scanner(System.in);
		DiningPhilosophers demo = new DiningPhilosophers();
		while (true) {
			Philosopher.reset();
			Chopstick.reset();
			demo.run();
			System.out.println("Press enter to retry...");
			scnr.nextLine();
		}
	}

	@Override
	public void run() {
		Philosopher[] philosophers = new Philosopher[NUM_OF_PHILOSOPHERS];
		Chopstick[] chopsticks = new Chopstick[NUM_OF_PHILOSOPHERS];

		for (int i = 0; i < NUM_OF_PHILOSOPHERS; i++) {
			chopsticks[i] = new Chopstick();
		}

		for (int i = 0; i < NUM_OF_PHILOSOPHERS; i++) {
			Chopstick leftChopstick = chopsticks[i];
			Chopstick rightChopstick = chopsticks[(i + 1) % NUM_OF_PHILOSOPHERS];
			philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
		}

		ExecutorService es = Executors.newFixedThreadPool(NUM_OF_PHILOSOPHERS);
		for (Philosopher philosopher : philosophers) {
			es.execute(philosopher);
		}

		es.shutdown(); // notify to stop accept new task
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // wait on existing tasks to finish
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("\nAll philosophers are dead.");
		System.out.println("====================================");
	}
}
