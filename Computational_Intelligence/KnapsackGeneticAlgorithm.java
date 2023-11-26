import java.util.Arrays;
import java.util.Random;

class Item {
    int weight;
    int value;

    Item(int weight, int value) {
        this.weight = weight;
        this.value = value;
    }
}

class Individual {
    int[] genes;
    int fitness;

    Individual(int size) {
        genes = new int[size];
        fitness = 0;
    }
    public String toString(){
        return Arrays.toString(genes)+ " fitness = " + fitness + '\n';
    }

    void generateIndividual() {
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = rand.nextInt(2); // Generate either 0 or 1 for genes
        }
    }

    void calculateFitness(Item[] items, int knapsackCapacity) {
        int totalWeight = 0;
        int totalValue = 0;

        for (int i = 0; i < genes.length; i++) {
            if (genes[i] == 1) {
                totalWeight += items[i].weight;
                totalValue += items[i].value;
            }
        }

        if (totalWeight > knapsackCapacity) {
            fitness = 0; // Penalize solutions that exceed the knapsack's capacity
        } else {
            fitness = totalValue;
        }
    }
}


class Population {
    Individual[] individuals;

    Population(int populationSize, int geneLength) {
        individuals = new Individual[populationSize];
        for (int i = 0; i < populationSize; i++) {
            individuals[i] = new Individual(geneLength);
        }
    }
    void initializePopulation() {
        for (Individual individual : individuals) {
            individual.generateIndividual();
        }
    }

    void calculateFitness(Item[] items, int knapsackCapacity) {
        for (Individual individual : individuals) {
            individual.calculateFitness(items, knapsackCapacity);
        }
    }

    Individual selectParent() {
        Random rand = new Random();
        return individuals[rand.nextInt(individuals.length)];
    }

    Population evolvePopulation(Item[] items, int knapsackCapacity) {
        Population newPopulation = new Population(individuals.length, items.length);

        for (int i = 0; i < individuals.length; i++) {
            Individual parent1 = selectParent();
            Individual parent2 = selectParent();

            Individual offspring = crossover(parent1, parent2);
            mutate(offspring);

            offspring.calculateFitness(items, knapsackCapacity);
            newPopulation.individuals[i] = offspring;
        }
        return newPopulation;
    }

    Individual crossover(Individual parent1, Individual parent2) {
        Individual offspring = new Individual(parent1.genes.length);
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(parent1.genes.length);

        for (int i = 0; i < parent1.genes.length; i++) {
            if (i < crossoverPoint) {
                offspring.genes[i] = parent1.genes[i];
            } else {
                offspring.genes[i] = parent2.genes[i];
            }
        }
        return offspring;
    }

    void mutate(Individual individual) {
        Random rand = new Random();
        int mutationPoint = rand.nextInt(individual.genes.length);
        individual.genes[mutationPoint] = 1 - individual.genes[mutationPoint];
    }
}

public class KnapsackGeneticAlgorithm {

    public static void main(String[] args) {
        Item [] items = {
                new Item(1600,13),
                new Item(250,15),
                new Item(600,13),
                new Item(500,8),
                new Item(1800,11),
                new Item(1200,10),
                new Item(100,6)
        };

        int knapsackCapacity = 4000;
        int populationSize = 10;
        int generations = 5;

        Population population = new Population(populationSize, items.length);
        population.initializePopulation();

        Individual fittestIndividual =getFittestIndividualAfterNGenerations(population,items,knapsackCapacity,generations);

        System.out.println("\nFittest Individual:");
        System.out.println("Genes: " + Arrays.toString(fittestIndividual.genes));
        System.out.println("Fitness: " + fittestIndividual.fitness);
    }

    private static Individual getFittestIndividualAfterNGenerations
            (Population population, Item[] items, int knapsackCapacity, int generations) {
        Individual fittestIndividual = null;
        for (int i = 0; i < generations; i++) {
            population.calculateFitness(items, knapsackCapacity);
            Individual fittest = getFittestIndividual(population);

            if(fittestIndividual==null || fittestIndividual.fitness<fittest.fitness){
                fittestIndividual = fittest;
            }
            System.out.println(Arrays.toString(population.individuals));
            System.out.println("Generation " + (i+1) + ", Fittest: " + fittest.fitness);

            population = population.evolvePopulation(items, knapsackCapacity);
        }
        return fittestIndividual;
    }

    static Individual getFittestIndividual(Population population) {
        Individual fittest = population.individuals[0]; // Assume the first individual is the fittest initially
        for (int i = 0; i < population.individuals.length; i++) {
            if (population.individuals[i].fitness > fittest.fitness) {
                fittest = population.individuals[i]; // Update the fittest individual if found
            }
        }
        return fittest;
    }

}
