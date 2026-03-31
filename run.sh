#!/bin/bash

# Student Grade Management System - Build and Run Script

echo "=========================================="
echo " Student Grade Management System"
echo "=========================================="
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Compile the project
echo "Compiling Java files..."
javac -d bin src/main/**/*.java src/main/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "Starting application..."
    echo "=========================================="
    echo ""
    
    # Run the application
    java -cp bin main.Main
else
    echo "✗ Compilation failed. Please check for errors."
    exit 1
fi
