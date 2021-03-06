package util;

public class Range {

    private int numberOfDimensions = 0;
    private int[] min;
    private int[] max;

    public static  Range EMPTY = OneDimensionalRange(0,0);

    public static Range OneDimensionalRange(int min, int max) {
        assert min <= max;
        return new Range(1, new int[]{min}, new int[]{max});
    }

    public static Range TwoDimensionalRange(int xMin, int xMax, int yMin, int yMax) {
        return new Range(2, new int[]{xMin, yMin}, new int[]{xMax, yMax});
    }

    private Range(int dimensions, int[] min, int[] max) {
        this.numberOfDimensions = dimensions;
        this.min = min;
        this.max = max;
    }

    public int dimensions() {
        return numberOfDimensions;
    }

    public int min(int dimension) {
        assert dimension < dimensions();
        return this.min[dimension];
    }

    public int max(int dimension) {
        assert dimension < dimensions();
        return this.max[dimension];
    }

    public boolean contains(int x) {
        if(numberOfDimensions != 1) {
            return false;
        }
        return (min[0] <= x && x <= max[0]);
    }

    public boolean contains(int x, int y) {
        if (numberOfDimensions != 2) {
            return false;
        }
        return (x >= min[0] && x <= max[0] &&y >= min[1] && y <= max[1]);
    }

    @Override
    public boolean equals(Object arg) {
        if(!(arg instanceof Range)) {
            return false;
        }
        Range that = (Range)arg;
        if(that.dimensions() != numberOfDimensions) {
            return false;
        }
        for(int i = 0; i < numberOfDimensions; i++) {
              if(that.min(i) != this.min(i) || that.max(i) != this.max(i)) {
                  return false;
              }
        }
        return true;
    }

    public boolean includes(Range that) {
        if(that.dimensions() != this.dimensions()) {
            return false;
        }
        for(int i = 0; i < numberOfDimensions; i++) {
            if(that.min(i) < this.min(i) || that.max(i) > this.max(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean overlaps(Range that) {
        if(that.dimensions() != this.dimensions()) {
            return false;
        }
        for(int i = 0; i < numberOfDimensions; i++) {
            if(that.max(i) < this.min(i) || that.min(i) > this.max(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean abuts(Range that) {
        if(that.dimensions() != this.dimensions()) {
            return false;
        }
        if (this.overlaps(that)) {
            return false;
        }
        for(int i = 0; i < numberOfDimensions; i++) {
            if(that.min(i) > this.max(i)+1) {
                return false;
            }
        }
        return true;
    }

    public boolean isContiguous(Range that) {
        if(this.dimensions() == 1) {
            return this.abuts(that);
        }
        if(!this.abuts(that)) {
            return false;
        }
        for(int i = 0; i < numberOfDimensions; i++) {
            if(that.min(i) == this.min(i) && that.max(i) == this.max(i)){
                return true;
            }
        }
        return false;
    }

    public Range combination(Range that) {
        if(!this.isContiguous(that))
            return Range.EMPTY;

        int[] rangeMin = new int[numberOfDimensions];
        int[] rangeMax = new int[numberOfDimensions];

        for(int i = 0; i < numberOfDimensions; i++) {
            rangeMin[i] = Math.min(that.min(i), this.min(i));
            rangeMax[i] = Math.max(that.max(i), this.max(i));
        }
        return new Range(numberOfDimensions, rangeMin, rangeMax);
    }
}
