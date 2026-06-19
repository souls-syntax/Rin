#include "Application.h"
#include <fstream>
#include <iostream>
#include <cstdlib>

namespace Rin {
    void Application::runFile(const std::string& path) {
        std::ifstream file(path, std::ios::binary);
        if (!file) {
            std::cerr << "Could not open file: " << path << "\n";
            std::exit(74);
        }
        std::string source((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());
        run(source);
    }
    void Application::runPrompt() {
        // TODO
    }

    void Application::run(const std::string& source) {
        // TODO
    }
}
