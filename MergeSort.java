public class MergeSort {

    // Recursive Merge Sort
    public static void mergeSort(PerformanceRecord[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;

        // Divide the array into two sections
        mergeSort(array, left, middle);
        mergeSort(array, middle + 1, right);

        // Merge the sorted sections
        merge(array, left, middle, right);
    }

    // Combines two sorted sections
    private static void merge(PerformanceRecord[] array, int left, int middle, int right) {
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        PerformanceRecord[] leftArray = new PerformanceRecord[leftSize];
        PerformanceRecord[] rightArray = new PerformanceRecord[rightSize];

        // Copy records into temporary arrays
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = array[left + i];
        }

        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = array[middle + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        // Compare and merge records by accuracy
        while (i < leftSize && j < rightSize) {
            if (leftArray[i].getAccuracy() <= rightArray[j].getAccuracy()) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }

            k++;
        }

        // Copy remaining records from the left array
        while (i < leftSize) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy remaining records from the right array
        while (j < rightSize) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
