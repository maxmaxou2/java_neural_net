import numpy as np
from scipy.ndimage import zoom
from scipy.ndimage import rotate
from skimage import io
import random
import os
import multiprocessing

w = 23
h= 32
angle_range = 5
contrast_factor_range = 0.1
brightness_factor_range = 256*0.1
mean = 0
std_dev = 0.3

nb_augment = 200

collectionId = "CEL"
folder_path = 'D:/BasicNeuralNetwork/Data/fastPokeTest'
files = os.listdir(folder_path)

def worker_function(original_image, resize_factors, fileName):
    for i in range(nb_augment) :
        image = np.copy(original_image)

        #Adjust contrast and brightness
        """image = (image - mean) * (1+random.uniform(-contrast_factor_range,contrast_factor_range)) + mean + random.uniform(-brightness_factor_range,brightness_factor_range)

        # Generate Gaussian noise
        noise = np.random.normal(mean, random.uniform(0,std_dev), image.shape).astype(np.uint8)

        #Rotate
        image = rotate(image + noise, random.uniform(-angle_range,angle_range), reshape=False)"""

        #Resize
        image = zoom(image, (resize_factors[1], resize_factors[0], 1), order=3)

        #Process
        image = np.clip(image, 0, 255).astype(np.uint8)

        #io.imsave('D:/BasicNeuralNetwork/Data/'+collectionId+'/'+fileName+"-"+str(i)+".png", image)
        io.imsave('D:/BasicNeuralNetwork/Data/fastPokeTest/'+fileName+"-"+str(i)+".png", image)

if __name__ == '__main__':
    processes = []
    for image_file in files:
        fileName = image_file.split(".")[0]
        image_path = os.path.join(folder_path, image_file)
        original_image = io.imread(image_path)
        mean = original_image.min()
        resize_factors = (w / original_image.shape[1], h / original_image.shape[0])
        process = multiprocessing.Process(target=worker_function, args=(original_image,resize_factors, fileName))
        processes.append(process)
        process.start()

    for process in processes:
        process.join()