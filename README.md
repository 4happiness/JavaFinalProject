# 葫芦娃与妖精的故事

## 故事的演绎

### 玩家操作指南：

两个玩家，一方点击打开服务器（一定要先打开服务器，并且只能由一方打开），然后一个玩家点击挑选为葫芦娃一方，一个玩家点击挑选为蛇精一方。

双方玩家按下空格，选择存放游戏记录的文件之后即可进入战场，开始游戏。

在游戏期间玩家可以通过上下左右键控制本方所选中的生物移动，可用Tab键切换控制的本方生物

移动撞击另一方生物即为对对方生物的攻击

在游戏未开始时或者游戏结束后，可以按下L键选择某游戏记录的文件进行战斗回放。

### 游戏界面截图

打开服务器界面：

![打开服务器](src\main\resources\READMEpicture\1.png)



游戏玩家选边界面

![选边](src\main\resources\READMEpicture\2.png)



双方选边结束，等待玩家按下空格开始游戏或者L键回放游戏

![双方都选边完毕，游戏可以开始](src\main\resources\READMEpicture\3.png)



选择记录文件界面

![选择记录文件](src\main\resources\READMEpicture\6.png)



游戏战斗界面

![游戏中](src\main\resources\READMEpicture\4.png)



游戏结束界面

![游戏结束](src\main\resources\READMEpicture\5.png)



## 故事的开始

### 选角

creature包下的类描绘了故事的各个角色。

葫芦娃与妖精是这个故事的主角，而他们都是生物，所以创建Creature这个基类，Creature这个基类包含了生物们一些共同的特征如生命力、速度、攻击力、防御力、阵营等，刻画了移动（move方法）、攻击（attack方法）等生物共同的行为，他们还都需要在战场上显示自己的存在(drawMyself方法）。葫芦阵营由Grandpa类、Hulu类和Pangolin类来刻画，妖精阵营由Snake类、Pangolin类、LittleMonster类来刻画，它们都继承自Creature类，设计Creature类是一个抽象类，因为在葫芦的世界里，需要有一个身份才能存在。

#### 多态的运用

每个生物都应该在战场当中展示自己的存在，但是他们展示的方式并不相同，通过重写drawMyself()方法，让每一个生物在战场上独一无二，展示自我。

### 战场

设计了一个Map类，Map类的主要功能就是绘制11*11的二维战场drawMap()和战场背景drawBackground()，在BattleField类中，战场中有creatures了，再用drawCreatures()画出生物，以及要圈出玩家选中的生物drawChosen()，统一在refreshField()方法中调用，利用一个线程每10ms刷新一次战场，展示出精彩的作战场景。

### 玩家交互

#### 点击Button事件

游戏初始界面中有三个Button

点击打开服务器Button 响应函数openSever() 打开服务器

点击Hulu Button 响应函数chooseHulu() 设置玩家模式为葫芦娃一方玩家模式，删除一些控件，开启键盘监听。

点击Snake Button 响应函数chooseSnake() 设置玩家模式为妖精一方玩家模式，删除一些控件，开启键盘监听。

#### 键盘响应事件

空格键 startGame() 创建出线程让自己方生物可以随机移动起来，创建战场刷新线程，战斗打响！再创建一个线程监听是否某一方的生物全部死掉（监听者）。 

L键 reviewGame() 在战斗未开始或者结束后选择战斗记录文件进行战斗回放（详情见故事的回顾篇）。

Tab键 shiftControl() 玩家可以控制自己一方的一个生物，按下Tab键可以转换控制另一个还存活的己方生物。具体实现，利用一个变量表示选择的生物下标，每个生物有个boolean变量表示自己是否正被玩家控制，通过drawChosen()框出被选中的那个生物。

方向键（上、下、左、右）作为方向参数传入move()方法  控制被选中生物上下左右移动。



## 大战打响

一个玩家可以控制一方的生物，当自己这一方进入到战斗状态以后，每一个生物都是一个线程，随机产生方向在战场中移动，玩家可以控制自己选中的人物如何移动。利用线程池来进行管理。

启动一个Timeline线程每隔10ms刷新战场，让玩家实时看到战斗状况。

启动一个监听者线程，通过wait()阻塞，等待某一方生物全部战死之后，再通过notifyall()唤醒该线程，调用gameOver()方法。

生物通过不断地移动来试图对对方生物进行攻击，如果在战场上某一格原来有一个地方生物，现在本方生物要移动到这一格，视为对敌方生物的一次猛烈撞击，对方生物会因此受到攻击而掉血。

### 多线程

对战的过程中有多个线程在同时工作，如生物线程、刷新界面线程、网络通信线程、监视线程等

在设计当中也注意到了多线程间的协同，避免出现线程不安全的情况

 如运用synchronized对资源加锁，使得对资源的访问顺序化，确保在某一时刻只有一个任务在使用共享资源（使其互斥）。比如在刷新界面时，希望界面显示的生物位置正是生物当前的位置，那么通过synchronized使得葫芦娃线程如果要移动那先在此时阻塞，不要移动，等界面刷新完成，再移动。生物的移动会影响Battelefiled.filed，所以多个线程不应该同时访问Battelefiled.filed，通过synchronized加锁，保证一次只有一个线程进入临界区。

另外用到了wait()和notifyAll()方法来控制线程间的协作，设计了一个监听游戏结束的线程，在游戏开始时通过wait()方法阻塞，待游戏有一方的生物全部战死（Creature.goodcreaturenum==0||Creature.badcreaturenum==0）时，使用notifyAll()方法，唤醒这一监听线程，然后调用gameOver()方法，进行游戏结束的处理。



### 葫芦娃和妖精也时髦地搞起了联网作战

网络对战实现的主要的类在network包下

在本次的设计当中采用的是端到端的通信方式，两方直接通信，虽然一方作为服务器，一方作为客户端，但是服务器也只是维护一方的数据，而不是维护全局数据，这样的p2p方式有一定的缺陷，但是效果似乎也还可以。通过提高通信的频率，以减小双发游戏交互的延迟。

网络通信的架构是tcp 服务器-客户端架构，一个服务器，一个客户端，模拟端到端通信。

网络通信也用到了对象的序利化，所以设计NetMessage类实现了Serializable接口。

双发通信的Message由NetMessage类刻画，其数据成员是一个三元组<x,y,living>的list，用来通知对方——本方生物当前的位置（x,y）以及对方生物当前的血量（living）。

开启客户端之后，客户端等待另一个玩家上线，双发建立连接之后，每一方都开启了两个线程，一个SendMessageThread线程，一个HandlerThread线程。

由于是端到端的通信，对方并不知道我方生物的移动和攻击情况，所以SendMessageThread线程的工作就是由当前战场生物（Battelefiled.creatures）的情况创建一个NetMessage对象，然后通过网络发送出去。

HandlerThread线程的工作就是不断从流当中读取NetMessage对象，然后根据message中的信息，更新对方生物的位置信息以及本方生物的血量，维护BatteleField.field，如果发现对面已经全部杀掉了本方的生物，则需要用notifyall()方法唤醒监听游戏结束的进程。



## 故事的回忆

回放功能的实现的类在playback包下

在刷新战场状况的方法refreshField()中，会向log文件中写入一个BattleLog对象，以记录当前的游戏状况，refreshField()方法每10ms被战场展示刷新线程timeline调用一次，所以程序每10ms也会写入一次游戏状况。

BattleLog记录游戏过程中生物的状况，并记录这局游戏的胜负。

通过一个CreatureLog对象的list来记录当前游戏中生物的信息。

CreatureLog对象主要记录了生物当前的生命值、位置、是否被玩家控制等信息。其实一开始是想通过直接序列化Creature去做的，但是发现不管程序中的BatteleFiled.creatures这个对象发生怎样的变化，序列化写入的信息都是一样的，后来查资料才发现原来如果每次序列化写同一个对象，不管这个对象变了没有，写的都是前面序列化记录的一个引用而已，这也是为什么我发现一开始序列化Creature去做的时候log文件那么短小的原因，原来并不会再次序列化写入，只是写了一个引用！所以后来改成了现在的方式。

LogManager类描述的是整个回放功能的控制者。核心方法review()，逻辑也很简单，创建一个用来回放战场的线程timeline，不断地从log文件中读取BattleLog对象，从读出来的对象中，将生物按照记录的位置显示在战场上，框出chosen为true的生物，在游戏结束后，根据BattleLog对象result成员，展示游戏结果。



## 故事的结尾

### 测试葫芦娃

生物的行为在战斗中显得很重要，主要从测试了生物的两个行为：isalive()和move()

isalive()的测试很简单，设置血量为0，测试其是否返回false

move()的测试分了三种情况，一种是下一步移动到的block并没有生物，检查生物move后的位置。第二种是下一步移动到的block已经有了一个同阵营的生物，那么该生物就不应该移动，检查其位置是否不变。第三种是下一步移动到的block已经有了一个不是同一阵营的生物，那么该生物就应该发动攻击，检查受攻击的生物血量是否符合预期。

测试了服务器和客户端的连接

当服务器未打开时，客户端连接服务器应该抛出ConnectException.

### 打包

使用了`maven-assembly-plugin`，maven package打包之后生成带有全部依赖的 `HuluWar-1.0-SNAPSHOT-jar-with-dependencies` jar 包，并设置了主入口为Main。可以直接双击执行或者命令行java -jar HuluWar-1.0-SNAPSHOT-jar-with-dependencies来运行。



## 心得体会与总结

在本次大作业的设计当中，尽量融入了在这学期的课程中所学习的Java面向对象程序设计方法，也尽量使得自己的代码贴近所学习的设计原则和运用一些设计模式，但是在总体的框架设计方面还有待加强。

初步接触多线程编程，在一开始的时候由于没有太关注多线程间的协同，出现了一些意想不到的bug，通过对临界区加锁，逐步解决了这些问题。

在完成回写功能时，生物对象的序列化遇到了一个较大的问题，就是发现序列化如果写入的同一个对象，不管这个对象在程序后来的执行中有没有变，序列化是不会向文件中又写入一次这个对象的，仅仅是写的第一次序列化的引用，这就导致回放的时候画面不动，经过调试和看Java序列化的设计，最终解决了此问题。

网络部分的设计还可以更加优美，可以采用一服务器两客户端的模式代替现行方案，更有利于游戏双方的数据同步。

通过本次大作业，Java程序设计的编程能力确实得到了提升。

谢谢老师和助教们一学期的辛勤授课，也祝2021更好。