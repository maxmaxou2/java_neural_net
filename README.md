# Pokemon Card Neural Network Classifier

A Java-based neural network implementation for classifying Pokemon card images. This project implements a custom neural network architecture with support for multi-threaded training and data augmentation.

## DISCLAIMER

This project was my first attempt ever using Neural Networks. Be kind please

## Features

- Custom neural network implementation with configurable layers
- Multi-threaded batch training for improved performance
- Data augmentation capabilities for training data
- Support for serialization and deserialization of trained models
- Progress tracking and accuracy reporting during training
- Image preprocessing and resizing utilities

## Project Structure

- `Main.java` - Main entry point and training loop implementation
- `NeuralNetwork.java` - Core neural network implementation
- `Layer.java` - Neural network layer implementation
- `DataLoader.java` - Data loading and batch management
- `DataScraper.java` - Data collection utilities
- `PokemonScraper.java` - Pokemon-specific data collection
- `DataAugment.py` - Python script for data augmentation
- `ImageResizer.java` - Image preprocessing utilities

## Requirements

- Java Runtime Environment (JRE)
- Python (for data augmentation)
- Pokemon card image dataset

## Usage

1. Prepare your dataset:
   - Place Pokemon card images in the `Data/Pokemon/ResizedCardImages/CEL` directory
   - Place corresponding labels in `Data/Pokemon/CardNames/CEL.txt`

2. Configure training parameters in `Main.java`:
   ```java
   public static float learnRate = 0.5f;
   public static float dataSetCoverage = 1f;
   public static int epochNumbers = 1000;
   public static int batchSize = 50;
   public static int threadNumber = 4;
   ```

3. Run the training:
   ```bash
   javac *.java
   java Main
   ```

## Model Architecture

The default network architecture consists of:
- Input layer: 1104 nodes
- Hidden layer 1: 786 nodes
- Hidden layer 2: 400 nodes
- Output layer: 50 nodes

## Training Process

1. The network loads or initializes a new model
2. Training data is loaded and preprocessed
3. Training proceeds in epochs with multi-threaded batch processing
4. After each epoch:
   - Model is saved
   - Test accuracy is reported
   - Progress is displayed

## Data Augmentation

The project includes a Python script (`DataAugment.py`) for augmenting the training dataset with various transformations to improve model robustness.

## Model Persistence

Trained models are saved with the `.ser` extension and can be loaded for continued training or inference.