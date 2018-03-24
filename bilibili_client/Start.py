import sys
from PyQt5.QtWidgets import QWidget, QLabel, QLineEdit, QGridLayout, QApplication, \
    QDesktopWidget, QPushButton, QColorDialog, QFrame
from PyQt5.QtGui import QColor, QIcon
from Windows import *
from MySocket import *


class Start(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindow()
        self.initUI()
        self.show()

    def setWindow(self):
        self.resize(400, 180)#设置窗口大小
        self.setCenter()#设置窗口居中(自定义函数)
        self.setWindowTitle('弹幕互动系统')#设置窗口标题
        self.setWindowIcon(QIcon('icon.ico'))  # 设置窗口图标

    def setCenter(self):#直接复制的，不知道什么原理，反正能居中6
        qr = self.frameGeometry()
        cp = QDesktopWidget().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def initUI(self):
        serverAddress = QLabel('服务器地址 : ')
        username = QLabel('用  户  名 : ')
        fontSize = QLabel('  字  号   : ')
        speed = QLabel('速度(秒) : ')
        fontColorButton = QPushButton('颜色', self)
        buttonStart = QPushButton('连接', self)
        self.serverAddressEdit = QLineEdit()
        self.serverAddressEdit.setText('sysustudentunion.cn')
        self.usernameEdit = QLineEdit()
        self.fontSizeEdit = QLineEdit()
        self.fontSizeEdit.setText('30')
        self.speedEdit = QLineEdit()
        self.speedEdit.setText('7')
        grid = QGridLayout()
        grid.setSpacing(10)
        grid.addWidget(serverAddress, 1, 0)
        grid.addWidget(self.serverAddressEdit, 1, 1)
        grid.addWidget(username, 2, 0)
        grid.addWidget(self.usernameEdit, 2, 1)
        grid.addWidget(fontSize, 3, 0)
        grid.addWidget(self.fontSizeEdit, 3, 1)
        grid.addWidget(speed, 4,0)
        grid.addWidget(self.speedEdit, 4, 1)
        grid.addWidget(fontColorButton, 5, 0)
        grid.addWidget(buttonStart, 5, 1)
        self.setLayout(grid)
        buttonStart.clicked.connect(self.connectServer)
        fontColorButton.clicked.connect(self.showColorDialog)
        self.fontColor = QColor(0, 0, 0)

    def showColorDialog(self):
        self.frm = QFrame(self)
        self.frm.setStyleSheet("QWidget { background-color: %s }"
                               % self.fontColor.name())
        self.frm.setGeometry(130, 22, 100, 100)
        self.fontColor = QColorDialog.getColor()
        if self.fontColor.isValid():
            self.frm.setStyleSheet("QWidget { background-color: %s }"
                                   % self.fontColor.name())

    def connectServer(self):
        serverAddress = self.serverAddressEdit.text()
        username = self.usernameEdit.text()
        try:
            fontSize = int(self.fontSizeEdit.text())
        except Exception:
            self.fontSizeEdit.setText('30')
            return
        try:
            speed = int(self.speedEdit.text())
        except Exception:
            self.speedEdit.setText('7')
            return
        socket = MySocket(serverAddress, username)
        self.main = Window(socket, fontSize, self.fontColor, speed)
        self.close()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    start_window = Start()
    sys.exit(app.exec_())
