package com.tabjy.dining_philosophers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Philosopher implements Runnable {

	private static String[] WELL_KNOWN_NAMES = new String[] {"Socrates", "Plato", "Aristotle", "Hegel", "Nietzsche"};
	private static int MAX_ACTION_DURATION = 200;
	public static int TIME_TO_STARVE_MS = 200;
	private static int ID_INC = 0;

	private enum State {
		THINKING, EATING, DEAD
	}

	private final int id = ID_INC++;
	private final String name;
	private final Chopstick leftChopstick;
	private final Chopstick rightChopstick;

	private State state = State.THINKING;

	public Philosopher(Chopstick leftChopstick, Chopstick rightChopstick) {
		this.leftChopstick = leftChopstick;
		this.rightChopstick = rightChopstick;

		if (id < WELL_KNOWN_NAMES.length) {
			name = WELL_KNOWN_NAMES[id];
		} else {
			name = "philosopher #" + id;
		}
	}

	public static void reset() {
		ID_INC = 0;
	}

	@Override
	public void run() {
		try {
			while (true) {
				switch (state) {
				case THINKING:
					think();
					break;
				case EATING:
					eat();
					break;
				case DEAD:
					die();
					return;
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void eat() throws InterruptedException {
		System.out.println(name + " started eating");
		if (!leftChopstick.pickUp(this, TIME_TO_STARVE_MS)) {
			state = State.DEAD;
			return;
		}

		if (!rightChopstick.pickUp(this, TIME_TO_STARVE_MS)) {
			state = State.DEAD;
			return;
		}

		doAction();

		rightChopstick.putDown();
		leftChopstick.putDown();

		state = State.THINKING;
	}

	public void think() throws InterruptedException {
		System.out.println(name + " started thinking");

		doAction();

		state = State.EATING;
	}

	private void die() {
		System.out.println(name + " starved after " + TIME_TO_STARVE_MS + "ms not eating.");
	}

	private void doAction() throws InterruptedException {
		Thread.sleep((long) (Math.random() * MAX_ACTION_DURATION));
	}

	@Override
	public String toString() {
		return "Philosopher{" + "id=" + id + ", name='" + name + '\'' + ", state=" + state + '}';
	}
}
