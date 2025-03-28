- 源项目地址：https://github.com/YoshikiHigo/TinyPDG.git

  进行修改的项目地址：

  #PS ：以下说明均在修改的项目上进行说明 
  ### 项目说明
  #### 1、运行
  - 文件 ` \TinyPDG\src\yoshikihigo\tinypdg\graphviz\Writer.java ` 为程序入口
  - 运行后在控制台输入待处理数据集位置即可
  #### 2、修改：
  **Writer.java**
  - 需要或者建议修改文件输出位置
  ![](./attachments/Pasted%20image%2020250328142718.png)
  - 对程序进行了try-catch处理，避免了程序中断导致数据集处理不完，并将无法处理的数据保存到了errFile中
  ![](./attachments/Pasted%20image%2020250328143014.png)
  ![](./attachments/Pasted%20image%2020250328143246.png)
  **PDGNode.java**
  - **行号**的输出，中间有一版没有行号的输出，是否需要行号可修改这里
  ![](./attachments/Pasted%20image%2020250328144458.png)
  - 函数的范围在`writer.java`中
  ![](./attachments/Pasted%20image%2020250328144615.png)