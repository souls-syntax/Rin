#include "Application.h"
#include <iostream>
#include <vector>

int main(int argc, char* argv[]) {
    std::vector<std::string> args(argv + 1, argv + argc);
    Rin::Application app;
    
    if (args.size() > 1) {
        std::cout << "Usage: rin [script]\n";
        std::exit(64);
    } else if (args.size() == 1) {
        app.runFile(args[0]);
    } else {
        app.runPrompt();
    }
}
