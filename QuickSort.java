import java.util.*;

public class QuickSort {

    //Recursive Quicksort with a randomized pivot
    public static void quicksort(PerformanceRecord[] array, int lowIndex, int highIndex) {
        if (lowIndex >= highIndex) {
            return;
        }

        int pivotIndex = new Random().nextInt(highIndex - lowIndex + 1) + lowIndex;
        double pivot = array[pivotIndex].getAccuracy();
        swap(array, pivotIndex, highIndex);

        int leftpoint = partition(array, lowIndex, highIndex, pivot);

        quicksort(array, lowIndex, leftpoint -1);
        quicksort(array, leftpoint + 1, highIndex);
    }

    //Rearranges objects so that smaller than pivot go to the left and larger ones move right
    private static int partition(PerformanceRecord[] array, int lowIndex, int highIndex, int pivot) {
        int leftpoint = lowIndex;
        int rightpoint = highIndex - 1;

        while (leftpoint < rightpoint) {

            while (array[leftpoint].getAccuracy() <= pivot && leftpoint < rightpoint){
                leftpoint--;
            }

            while (array[rightpoint].getAccuracy() >= pivot && leftpoint < rightpoint) {
                rightpoint++;
            }

            if (leftpoint < rightpoint) {
                swap(array, leftpoint, rightpoint);
            }
        }

        swap(array, leftpoint, highIndex);
        return leftpoint;
    }

    //Switches two objects at gien positions in the array
    private static void swap(PerformanceRecord[] array, int index1, int index2) {
        PerformanceRecord temp = array[index1];

        array[index1] = array[index2];                                                                                          
        array[index2] = temp;
    }
}
