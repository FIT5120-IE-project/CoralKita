import tensorflow as tf
from tensorflow.keras import datasets, layers, models
from tensorflow.keras.models import Sequential
import matplotlib.pyplot as plt
import cv2
import numpy as np
import pathlib
'''
图片多分类问题
'''

data_dir ="img_data"
data_dir = pathlib.Path(data_dir)
#查看数据的长度
image_count = len(list(data_dir.glob('*/*.jpg')))
print(image_count)
#开始处理输入的数据集
batch_size = 32
img_height = 180
img_width = 180

train_ds = tf.keras.utils.image_dataset_from_directory(
  data_dir,
  validation_split=0.2,
  subset="training",
  seed=123,
  image_size=(img_height, img_width),
  batch_size=batch_size)

val_ds = tf.keras.utils.image_dataset_from_directory(
  data_dir,
  validation_split=0.2,
  subset="validation",
  seed=123,
  image_size=(img_height, img_width),
  batch_size=batch_size)

class_names = train_ds.class_names
num_classes = len(class_names)
print(class_names)

# plt.figure(figsize=(10, 10))
# for images, labels in train_ds.take(1):
#   for i in range(9):
#     ax = plt.subplot(3, 3, i + 1)
#     plt.imshow(images[i].numpy().astype("uint8"))
#     plt.title(class_names[labels[i]])
#     plt.axis("off")
#

# #1.开始真正建模
# model = Sequential([
#   layers.Rescaling(1./255,input_shape=(img_height, img_width, 3)),
#   layers.Conv2D(16, 3, padding='same', activation='relu'),
#   layers.MaxPooling2D(),
#   layers.Conv2D(32, 3, padding='same', activation='relu'),
#   layers.MaxPooling2D(),
#   layers.Conv2D(64, 3, padding='same', activation='relu'),
#   layers.MaxPooling2D(),
#   layers.Dropout(0.2),
#   layers.Flatten(),
#   layers.Dense(128, activation='relu'),
#   layers.Dense(num_classes)
# ])
# plt.show()
# 开始真正意义上的建模
# 为了防止图片数据过少，我们采用把原始图片进行选择、缩放等操作，增加训练样本，这样可以防止过拟合导致的训练表现很好，测试表现
data_augmentation = tf.keras.Sequential(
  [
    layers.RandomFlip("horizontal",
                      input_shape=(img_height,
                                  img_width,
                                  3)),
    layers.RandomRotation(0.1),   #随机的旋转的幅度
    layers.RandomZoom(0.1),     #随机的放大或缩小的幅度
  ]
)
# plt.figure(figsize=(10, 10))
# for images, _ in train_ds.take(1):
#   for i in range(9):
#     augmented_images = data_augmentation(images)
#     ax = plt.subplot(3, 3, i + 1)
#     plt.imshow(augmented_images[0].numpy().astype("uint8"))
#     plt.axis("off")
# plt.show()
model = Sequential([
  data_augmentation,
  layers.Rescaling(1./255),
  layers.Conv2D(16, 3, padding='same', activation='relu'),
  layers.MaxPooling2D(),
  layers.Conv2D(32, 3, padding='same', activation='relu'),
  layers.MaxPooling2D(),
  layers.Conv2D(64, 3, padding='same', activation='relu'),
  layers.MaxPooling2D(),
  layers.Dropout(0.2),
  layers.Flatten(),
  layers.Dense(128, activation='relu'),
  layers.Dense(num_classes)
])

# 编译模型，相当于进行制定一个计划,也可以叫懒加载
model.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

print(model.summary())
#
# #开始训练模型，fit 相当与一个action。给模型输入数据，
epochs=10
history = model.fit(
  train_ds,
  validation_data=val_ds,
  epochs=epochs
)
#保存模型，方便后续预测调用
model.save("mode/my_img_class.h5")
#可视化最终的一个训练结果
acc = history.history['accuracy']
val_acc = history.history['val_accuracy']

loss = history.history['loss']
val_loss = history.history['val_loss']

epochs_range = range(epochs)
#可视化查看训练结果
plt.figure(figsize=(8, 8))
plt.subplot(1, 2, 1)
plt.plot(epochs_range, acc, label='Training Accuracy')
plt.plot(epochs_range, val_acc, label='Validation Accuracy')
plt.legend(loc='lower right')
plt.title('Training and Validation Accuracy')

plt.subplot(1, 2, 2)
plt.plot(epochs_range, loss, label='Training Loss')
plt.plot(epochs_range, val_loss, label='Validation Loss')
plt.legend(loc='upper right')
plt.title('Training and Validation Loss')
plt.show()

# # #加载模型
# mode_path="mode/my_img_class.h5"
# model=models.load_model(mode_path)
#
# dog_01_path="img_test/chicken/1009.jpg"
# # 用tf的工具来处理输入图片
# img = tf.keras.utils.load_img(
#     dog_01_path, target_size=(180, 180)
# )
# dog_01 = tf.keras.utils.img_to_array(img)
#
# dog_01 = tf.expand_dims(dog_01, 0) # Create a batch
# print(dog_01.shape)
#
# #预测的结果为一个概率表[[],[]] ,之所以为这样的结构，是因为你可以同时预测多张图片
# result_p=model.predict(dog_01)[0]
# for p,re in zip(result_p,class_names):
#     print("{}的概率为{}".format(re,p))
# #取出概率最大的值所在索引，并映射出原来的分类标准
# max_index=list(np.where(result_p==np.max(result_p)))[0][0]
# #
# print("最终预测的结果为：{}".format(class_names[max_index]))

