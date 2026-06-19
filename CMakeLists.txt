cmake_minimum_required(VERSION 3.25)

project(rin)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED YES)
set(CMAKE_CXX_EXTENSION OFF)

set( SOURCE_FILES
	src/Application.cpp
    src/Main.cpp
)

add_executable(${PROJECT_NAME} ${SOURCE_FILES})

target_include_directories(${PROJECT_NAME}
	PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/src/include"
)

target_compile_options(${PROJECT_NAME} PRIVATE
	$<$<AND:$<CONFIG:Debug>,$<COMPILE_LANGUAGE:CXX>>:-O0 -Wall -Wextra -Wconversion -Wsign-conversion -Werror -pedantic-errors -ggdb>
	$<$<AND:$<CONFIG:Release>,$<COMPILE_LANGUAGE:CXX>>:-O2 -DNDEBUG>
)


set(RIN_SANTITIZER "none" CACHE STRING "Santitizer to use")

if(RIN_SANTITIZER STREQUAL "Address")
	target_compile_options(${PROJECT_NAME} PRIVATE
		$<$<AND:$<CONFIG:Debug>,$<COMPILE_LANGUAGE:CXX>>:-fsanitize=address,undefined,leak -fno-omit-frame-pointer>
	)
	target_link_options(${PROJECT_NAME} PRIVATE
		$<$<CONFIG:Debug>:-fsanitize=address,undefined,leak>
	)
elseif(RIN_SANTITIZER STREQUAL "Thread")
	target_compile_options(${PROJECT_NAME} PRIVATE
		$<$<AND:$<CONFIG:Debug>,$<COMPILE_LANGUAGE:CXX>>:-fsanitize=thread>
	)
	target_link_options(${PROJECT_NAME} PRIVATE
		$<$<CONFIG:Debug>:-fsanitize=thread>
	)
endif()

enable_testing()
if(EXISTS "${CMAKE_CURRENT_SOURCE_DIR}/tests/CMakeLists.txt")
	add_subdirectory(tests)
endif()
