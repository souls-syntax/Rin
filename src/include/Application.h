#pragma once
#include <string>

namespace Rin {
    class Application {
	public:
        	void runFile(const std::string& path);
        	void runPrompt();
 	private:
        	void run(const std::string& source);
    };
}
