#ifndef QTMAINTHREAD_H
#define QTMAINTHREAD_H
#include <QThread>

#include <QCoreApplication>
#include <qdebug.h>
#include <qtmyobj.h>

// This is the thread where the Qt application runs
class QtMainThread : public QThread
{
    Q_OBJECT

    QCoreApplication *app;
    QtMainThread(){};

    //**** HERE THE QObject pointers that will be exported to JAVA **/
public:
    QtMyObj* instance_qtMyObj;

public:
    static QtMainThread& getInstance(){
        static QtMainThread instance;
        return instance;
    }

    void stopApplication(){
        if (app != NULL){
            app->exit();
        }
    }

private:
    void run(){
        int argc=0;
        char* argv[0];
        app = new QCoreApplication(argc, argv);
        connect(app, SIGNAL(destroyed()), this, SLOT(appTerminated()));
        qDebug() << "QCoreApplication running in a separate thread!";

        // Create an instance of my object
        QtMyObj myObj;
        instance_qtMyObj = &myObj;

        // runs the eventloop
        app->exec();
        qDebug() << "BYE BYE";
    }

 private slots:
    void appTerminated(){
        qDebug() << "QtCoreApplication terminated";
    }
};
#endif // QTMAINTHREAD_H
