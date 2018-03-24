import sys
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
from random import *
from time import *
from MySocket import *

class MyLable(QLabel):
    def __init__(self, parent, str):
        super().__init__(parent.fontStyle + str + '''</p>''', parent)
        self.step = parent.screen.width()/parent.speed
        self.x = parent.screen.width() + randint(0, int(self.step * 10))
        self.y = randint(0, parent.screen.height()-self.height())
        self.move(self.x, self.y)
        self.time = time()

    def next(self) -> object:
        self.x -= self.step * (time()-self.time)
        self.time = time()
        self.move(self.x, self.y)
        return self.x > -self.width()

    def out(self):
        return self.x < -self.width()

class Window(QWidget):
    def __init__(self, socket, fontSize, fontColor, speed):
        super(Window, self).__init__(None)#调用父构造函数初始化
        self.fontStyle = '''<p style="color:rgb(''' + \
                         str(int(fontColor.name()[1:3], 16)) + ',' + \
                         str(int(fontColor.name()[3:5], 16)) + ',' + \
                         str(int(fontColor.name()[5:7], 16)) + \
                         ');font-size:' + str(fontSize) + '''px;">'''
        self.socket = socket
        self.fontSize = fontSize
        self.fontColor = fontColor
        self.speed = speed
        self.setWindow()#设置窗口
        self.setButton()  # 设置按钮(后期删！）
        self.barrage = [] # 储存弹幕
        self.setTimer() #设置刷新时间间隔
        tmp_label = MyLable(self, '程序已经正常启动')
        self.barrage.append(tmp_label)
        self.show()

    def setWindow(self):
        self.setAttribute(Qt.WA_TranslucentBackground, True) #设置透明背景
        self.setWindowFlags(Qt.WindowStaysOnTopHint | Qt.FramelessWindowHint) #设置置顶和无边框
        self.screen = QDesktopWidget().screenGeometry()  # 获取屏幕分辨率
        self.setGeometry(0, 0, self.screen.width(), self.screen.height())  # 设置窗口位置XY及大小XY

    def setButton(self):
        QToolTip.setFont(QFont('SansSerif', 10))#设置提示框字体与字号
        buttonRestart = QPushButton('×', self)#创建重新开始按钮
        buttonRestart.resize(20, 20)
        buttonRestart.move(0, 0)
        buttonRestart.setToolTip('Click here to close')
        buttonRestart.clicked.connect(self.close)

    def setTimer(self):
        self.timer = QTimer()
        self.frequency = 30
        self.timer.setInterval(self.frequency)
        self.timer.timeout.connect(self.Redraw)
        self.timer.start()
        self.countTime = time() + 10

    def Redraw(self):
        try:
            if time() - self.countTime >= 10:
                self.countTime = time()
                data = self.socket.get()
                data = data[0:len(data)-2].split('\n')
                for i in data:
                    tmp_label = MyLable(self, i)
                    tmp_label.show()
                    self.barrage.append(tmp_label)
            self.barrage = [ i for i in self.barrage if i.next()]
        except Exception:
            tmp_label = MyLable(self, '系统出现未知错误！')
            tmp_label.show()
            self.barrage.append(tmp_label)



if __name__ == '__main__':
    app = QApplication(sys.argv)
    fontColor = QColor(23, 23, 255)
    socket = MySocket('sysustudentunion.cn', 'tu_4s2q3o3p4928')
    window = Window(socket, 30, fontColor, 7)
    #app.exec_()
    sys.exit(app.exec_())
