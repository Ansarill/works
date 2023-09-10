#include <fstream>
#include <time.h>
#include <omp.h>
#include <iostream>
#include <string>

using namespace std;

int n;
double r;

// Usage OpenMp for boosting algorithms. Here represented paralleled Monte Carlo algorithm.

bool pointIn(double x, double y) {
    return x * x + y * y <= r * r;
}

double getRand() {
    return (rand() / 32767.0 - 0.5) * 2 * r;
}


int main(int argc, char *argv[]) {
    if (argc < 4) {
        cout << ("not enough arguments, example: " +
                 (string) "omp4 <number of threads> <input file name> <output file name>");
        return 0;
    }
    int threadCnt = stoi(argv[1]);
    threadCnt = 12;
    if (threadCnt < -1 && threadCnt == 0) {
        cout << ("thread count of '" + (string) argv[1] + "' isn't supported");
        return 0;
    } else if (threadCnt > 0) {
        omp_set_num_threads(threadCnt);
    }
    ifstream in(argv[2]);
    if (!in) {
        cout << "Couldn't open file for reading: '" + (string) argv[2] + "'";
        return 0;
    }
    in >> r >> n;
    if (r <= 0 || n <= 0) {
        cout << "n/r isn't supported";
        return 0;
    }
    in.close();
    srand(time(0));
    double start = omp_get_wtime();
    int circ = 0;
#pragma omp parallel if (threadCnt != -1)
    {
#pragma omp for schedule(static)
        for (int i = 0; i < n; i++) {
            if (pointIn(getRand(), getRand())) {
#pragma omp atomic
                circ++;
            }
        }
    }
    double end = omp_get_wtime();
    printf("Time (%i thread(s)): %g ms\n", threadCnt > 0 ? threadCnt : 1, (end - start) * 1000);
    ofstream out(argv[3]);
    if (!out) {
        cout << ("Couldn't open file for writing: '" + (string) argv[2] + "'");
        return 0;
    }
    out << (4 * r * r * circ) / n;
    out.close();
}