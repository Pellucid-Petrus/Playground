#include <QCoreApplication>
#include <qdebug.h>
#include <qtmyobj.h>

// The main is not the entrypoint for this app if it's called from java
int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    qDebug() << "QCoreApplication running!";

    // Create an instance of my object
    QtMyObj myObj;

    return a.exec();
}
