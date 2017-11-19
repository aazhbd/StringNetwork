/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "rpc.h"

/*get_path_1_svc: is a method
*Input: void
*Output: string
*Operation: will return the present working directory
*/
char **
get_path_1_svc(void *argp, struct svc_req *rqstp)
{
	static char *result;

	/*
	 * insert server code here
	 */
	FILE *fp = popen("pwd", "r");
	char bytes[1024];
	fgets(bytes, 128, fp);
	fclose(fp);
	result = bytes;
	return &result;
}

/*Ascending order - Bubble Sorting Implemented here*/
void bubble_sort(int list[], int n)
{
	long c, d, t;

	for (c = 0; c < (n - 1); c++)
	{
		for (d = 0; d < n - c - 1; d++)
		{
			if (list[d] > list[d + 1])
			{
				/* Swapping */
				t = list[d];
				list[d] = list[d + 1];
				list[d + 1] = t;
			}
		}
	}
}

/*get_sort_1_svc: is a method
*Input: sortElement; define in specification file - contains an int element and int array
*Output: sortElement
*Operation: will return sorted array
*/
sortElement *
get_sort_1_svc(sortElement *argp, struct svc_req *rqstp)
{
	static sortElement result;

	/*
	 * insert server code here
	 */
	int a[100], n, i;
	n = argp->len;
	
	for(i =0; i < n; i++){
		a[i] = argp->element[i];
		result.element[i] = a[i];
	}
	
	bubble_sort(result.element, n);
	return &result;
}

/*get_echo_1_svc: is a method
*Input: string
*Output: string
*Operation: will return the same message that recieved from client
*/
char **
get_echo_1_svc(char **argp, struct svc_req *rqstp)
{
	static char *result;

	/*
	 * insert server code here
	 */
	result = *argp;
	return &result;
}


/*get_check_1_svc: is a method
*Input: string
*Output: string
*Operation: will return the file status to client (File exists/Doesn't exixts)
*/
char **
get_check_1_svc(char **argp, struct svc_req *rqstp)
{
	static char *result;

	/*
	 * insert server code here
	 */

	result = *argp;
	FILE *fp;
	if ((fopen(result,"r"))!=NULL){
		result = "File Exists";
	}
	else
	{
		result = "File Doesn't Exists";
	}
	return &result;
}

/*Matrix multiplication - using 1D array*/
void multiplyMatrices(int* matA, int rA, int cA, int* matB, int rB, int cB, int* matC, int rC, int cC) {
    int i, j, k, sum;
    for (i = 0; i < rA; i++) {
        for (j = 0; j < cB; j++) {
            sum = 0;
            for (k = 0; k < rB; k++){
                sum = sum + matA[i * cA + k] * matB[k * cB + j];
            }
            matC[i * cC + j] = sum;
        }
    }
}

/*get_matmul_1_svc: is a method
*Input: matrixMulElements; define in specification file - contains three int elements and three int arrays
*Output: matrixMulElements
*Operation: will return the multiplication value of two matrices
*/
matrixMulElements *
get_matmul_1_svc(matrixMulElements *argp, struct svc_req *rqstp)
{
	static matrixMulElements  result;

	/*
	 * insert server code here
	 */
	int matA[100], matB[100], matC[100], r1, c1, c2;
	r1 = argp->r1;
	c1 = argp->c1;
	c2 = argp->c2;

	int i;

	for(i = 0; i< r1 * c1; i++){
		matA[i] = argp->matA[i];
	}

	for(i = 0; i< c1 * c2; i++){
		matB[i] = argp->matB[i];
	}
	
	multiplyMatrices(matA, r1, c1, matB, c1, c2, result.matC, r1, c2) ;

	return &result;
}
