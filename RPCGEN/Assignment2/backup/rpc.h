/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _RPC_H_RPCGEN
#define _RPC_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct matrixMulElements {
	int r1;
	int c1;
	int matA[100];
	int c2;
	int matB[100];
	int matC[100];
};
typedef struct matrixMulElements matrixMulElements;

struct sortElement {
	int element[1000];
	int len;
};
typedef struct sortElement sortElement;

#define RPC_PROG 0x31111111
#define RPC_VERS 1

#if defined(__STDC__) || defined(__cplusplus)
#define get_path 1
extern  char ** get_path_1(void *, CLIENT *);
extern  char ** get_path_1_svc(void *, struct svc_req *);
#define get_sort 2
extern  sortElement * get_sort_1(sortElement *, CLIENT *);
extern  sortElement * get_sort_1_svc(sortElement *, struct svc_req *);
#define get_echo 3
extern  char ** get_echo_1(char **, CLIENT *);
extern  char ** get_echo_1_svc(char **, struct svc_req *);
#define get_check 4
extern  char ** get_check_1(char **, CLIENT *);
extern  char ** get_check_1_svc(char **, struct svc_req *);
#define get_matMul 5
extern  matrixMulElements * get_matmul_1(matrixMulElements *, CLIENT *);
extern  matrixMulElements * get_matmul_1_svc(matrixMulElements *, struct svc_req *);
extern int rpc_prog_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define get_path 1
extern  char ** get_path_1();
extern  char ** get_path_1_svc();
#define get_sort 2
extern  sortElement * get_sort_1();
extern  sortElement * get_sort_1_svc();
#define get_echo 3
extern  char ** get_echo_1();
extern  char ** get_echo_1_svc();
#define get_check 4
extern  char ** get_check_1();
extern  char ** get_check_1_svc();
#define get_matMul 5
extern  matrixMulElements * get_matmul_1();
extern  matrixMulElements * get_matmul_1_svc();
extern int rpc_prog_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_matrixMulElements (XDR *, matrixMulElements*);
extern  bool_t xdr_sortElement (XDR *, sortElement*);

#else /* K&R C */
extern bool_t xdr_matrixMulElements ();
extern bool_t xdr_sortElement ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_RPC_H_RPCGEN */
