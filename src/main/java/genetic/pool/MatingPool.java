package genetic.pool;

import genetic.ADN;

import java.util.*;

abstract public class MatingPool<D> {

    protected Map<Interval, ADN<D>> pool;
    protected List<Integer> matingPoolIndexes;
    protected List<ADN<D>> origin;

    public MatingPool(List<ADN<D>> origin) {
        this.origin = origin;
        this.matingPoolIndexes = new ArrayList<>();
        this.pool = new HashMap<>();
    }

    abstract public ADN<D> getRandomParent();

    @Override
    public String toString() {
        return pool.toString();
    }
}
