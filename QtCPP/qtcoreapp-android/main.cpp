#include <QCoreApplication>
#include <qdebug.h>
#include <qtmyobj.h>

// The main is not the entrypoint for this app if it's called from java
int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    qDebug() << "QCoreApplication running!";

    // Create instances of my objects
    QtMyObj myObj;

    // Tells to Java that the app has been loaded

    return a.exec();
}
