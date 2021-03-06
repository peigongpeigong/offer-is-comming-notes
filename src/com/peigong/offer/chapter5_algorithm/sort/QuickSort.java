package com.peigong.offer.chapter5_algorithm.sort;

/**
 * @author: lilei
 * @create: 2020-06-04 16:22
 **/
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = new int[]{3, 6, 4, 7, 13, 65, 87, 33, 11};
        quickSort2(arr, 0, arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivotIndex = -1;
        //基准元素
        int pivot = arr[start];
        int left = start;
        int right = end;
        while (left != right) {
            //从右边找到第一个比基准元素小的
            while (left < right && arr[right] > pivot) {
                right--;
            }
            //从左边找到一个比基准元素大的
            while (right > left && arr[left] <= pivot) {
                left++;
            }
            //交换left和right的元素
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        //基准元素和left、right重合位置的元素交换
        arr[start] = arr[right];
        arr[right] = pivot;
        quickSort(arr, start, right - 1);
        quickSort(arr,right + 1, end);
    }

    public static void quickSort2(int[] arr, int start, int end) {
        if(start >= end) return;
        int left = start;
        int right = end;
        int p = arr[start];
        while (left != right) {
            while (left < right && arr[right] > p) {
                right--;
            }
            while (left < right && arr[left] <= p) {
                left++;
            }
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        arr[start] = arr[right];
        arr[right] = p;
        quickSort2(arr, start, right - 1);
        quickSort2(arr,right + 1,end);
    }

}
