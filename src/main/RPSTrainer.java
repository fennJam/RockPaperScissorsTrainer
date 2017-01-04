package main;

import java.util.Arrays;
import java.util.Random;


public class RPSTrainer {

	
	public static final int ROCK = 0, PAPER = 1, SCISSORS = 2, NUM_ACTIONS = 3;
	String[] actions = { "Rock", "Paper", "Scissors" };
	public static final Random random = new Random();
	double[] regretSum = new double[NUM_ACTIONS], strategy = new double[NUM_ACTIONS],
			strategySum = new double[NUM_ACTIONS], oppStrategy = strategy;

	public static void main(String[] args) {
		RPSTrainer trainer = new RPSTrainer();
		trainer.train(10000);
		System.out.println(Arrays.toString(trainer.getAverageStrategy()));
	}

	private double[] getStrategy() {
		double normalizingSum = 0;
		for (int a = 0; a < NUM_ACTIONS; a++) {
			strategy[a] = regretSum[a] > 0 ? regretSum[a] : 0;
			normalizingSum += strategy[a];
		}
		for (int a = 0; a < NUM_ACTIONS; a++) {
			if (normalizingSum > 0)
				strategy[a] /= normalizingSum;
			else
				strategy[a] = 1.0 / NUM_ACTIONS;
			strategySum[a] += strategy[a];
		}
		return strategy;
	}

	public int getAction(double[] strategy) {
		double r = random.nextDouble();
		int a = 0;
		double cumulativeProbability = 0;
		while (a < NUM_ACTIONS - 1) {
			cumulativeProbability += strategy[a];
			if (r < cumulativeProbability)
				break;
			a++;
		}
		return a;
	}

	public void train(int iterations) {
		double[] actionUtility = new double[NUM_ACTIONS];
		for (int i = 0; i < iterations; i++) {
			// Get regret-matched mixed-strategy actions
			double[] strategy = getStrategy();
			System.out.println(" Strategy:" + Arrays.toString(strategy));
			System.out.println(" StrategySum:" + Arrays.toString(strategySum));
			System.out.println("AverageStrategy:" + Arrays.toString(getAverageStrategy()));
			int myAction = getAction(strategy);
			int otherAction = getAction(oppStrategy);
			// Compute action utilities
			// set the utility of you using the same strategy as your opponent
			// to 0
			actionUtility[otherAction] = 0;
			// If their action is 2 set the utility of strategy 0 to +1
			// otherwise their action+1 to +1
			actionUtility[otherAction == NUM_ACTIONS - 1 ? 0 : otherAction + 1] = 1;
			// If their action is 0 set the utility of strategy 2 to -1
			// otherwise their action-1 to -1
			actionUtility[otherAction == 0 ? NUM_ACTIONS - 1 : otherAction - 1] = -1;
			// Accumulate action regret
			double[] regret = new double[NUM_ACTIONS];
			for (int a = 0; a < NUM_ACTIONS; a++) {
				regretSum[a] += actionUtility[a] - actionUtility[myAction];
				regret[a] = actionUtility[a] - actionUtility[myAction];
			}
			// String result;
			// if(actionUtility[myAction]>0){
			// result ="WIN!";
			// }else if(actionUtility[myAction]==0){
			// result = "DRAW!";
			// }
			// else{
			// result="LOSE!";
			// }
			//
			// System.out.println("I play:"+actions[myAction]+" === my opponent
			// plays:"+actions[otherAction]+"
			// RESULT:"+result+"\nRegret:"+Arrays.toString(regret)+"\nRegretSum:"+Arrays.toString(regretSum));
		}

	}

	public double[] getAverageStrategy() {
		double[] avgStrategy = new double[NUM_ACTIONS];
		double normalizingSum = 0;
		for (int a = 0; a < NUM_ACTIONS; a++)
			normalizingSum += strategySum[a];
		for (int a = 0; a < NUM_ACTIONS; a++)
			if (normalizingSum > 0)
				avgStrategy[a] = strategySum[a] / normalizingSum;
			else
				avgStrategy[a] = 1.0 / NUM_ACTIONS;
		return avgStrategy;
	}

}
