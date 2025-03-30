package org.apache.commons.math3.genetics;


public abstract class ListPopulation implements org.apache.commons.math3.genetics.Population {
    private java.util.List<org.apache.commons.math3.genetics.Chromosome> chromosomes;

    private int populationLimit;

    public ListPopulation(final int populationLimit) {
        this(java.util.Collections.<org.apache.commons.math3.genetics.Chromosome>emptyList(), populationLimit);
    }

    public ListPopulation(final java.util.List<org.apache.commons.math3.genetics.Chromosome> chromosomes, final int populationLimit) {
        if (chromosomes == null) {
            throw new org.apache.commons.math3.exception.NullArgumentException();
        }
        if (populationLimit <= 0) {
            throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, populationLimit);
        }
        if ((chromosomes.size()) > populationLimit) {
            throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, chromosomes.size(), populationLimit, false);
        }
        this.populationLimit = populationLimit;
        this.chromosomes = new java.util.ArrayList<org.apache.commons.math3.genetics.Chromosome>(populationLimit);
        this.chromosomes.addAll(chromosomes);
    }

    public void setChromosomes(final java.util.List<org.apache.commons.math3.genetics.Chromosome> chromosomes) {
        if (chromosomes == null) {
            throw new org.apache.commons.math3.exception.NullArgumentException();
        }
        if ((chromosomes.size()) > (populationLimit)) {
            throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, chromosomes.size(), populationLimit, false);
        }
        this.chromosomes.clear();
        this.chromosomes.addAll(chromosomes);
    }

    public void addChromosomes(final java.util.Collection<org.apache.commons.math3.genetics.Chromosome> chromosomeColl) {
        if (((chromosomes.size()) + (chromosomeColl.size())) > (populationLimit)) {
            throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, chromosomes.size(), populationLimit, false);
        }
        this.chromosomes.addAll(chromosomeColl);
    }

    public java.util.List<org.apache.commons.math3.genetics.Chromosome> getChromosomes() {
        return java.util.Collections.unmodifiableList(chromosomes);
    }

    protected java.util.List<org.apache.commons.math3.genetics.Chromosome> getChromosomeList() {
        return chromosomes;
    }

    public void addChromosome(final org.apache.commons.math3.genetics.Chromosome chromosome) {
        if ((chromosomes.size()) >= (populationLimit)) {
            throw new org.apache.commons.math3.exception.NumberIsTooLargeException(org.apache.commons.math3.exception.util.LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, chromosomes.size(), populationLimit, false);
        }
        this.chromosomes.add(chromosome);
    }

    public org.apache.commons.math3.genetics.Chromosome getFittestChromosome() {
        org.apache.commons.math3.genetics.Chromosome bestChromosome = this.chromosomes.get(0);
        for (org.apache.commons.math3.genetics.Chromosome chromosome : this.chromosomes) {
            if ((chromosome.compareTo(bestChromosome)) > 0) {
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
    }

    public int getPopulationLimit() {
        return this.populationLimit;
    }

    public void setPopulationLimit(final int populationLimit) {
        if (populationLimit <= 0) {
            throw new org.apache.commons.math3.exception.NotPositiveException(org.apache.commons.math3.exception.util.LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, populationLimit);
        }
        if (populationLimit < (chromosomes.size())) {
            throw new org.apache.commons.math3.exception.NumberIsTooSmallException(populationLimit, chromosomes.size(), true);
        }
        this.populationLimit = populationLimit;
    }

    public int getPopulationSize() {
        return this.chromosomes.size();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return this.chromosomes.toString();
    }

    public java.util.Iterator<org.apache.commons.math3.genetics.Chromosome> iterator() {
        return getChromosomes().iterator();
    }
}

