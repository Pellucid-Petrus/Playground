#ifndef QTMYOBJ_H
#define QTMYOBJ_H

#include <QObject>
#include <QDebug>

class QtMyObj : public QObject
{

    Q_OBJECT
    Q_PROPERTY(QString priority READ priority WRITE setPriority NOTIFY priorityChanged)

public:
    explicit QtMyObj(QObject *parent = 0) {
        Q_UNUSED(parent)
        qDebug() << "QtMyObj created";
    };

    void setPriority(QString priority)
    {
        m_priority = priority;
        emit priorityChanged(priority);
    }

    QString priority() const
    { return m_priority; }

signals:
    void priorityChanged(QString);

private:
    QString m_priority;
};

#endif // QTMYOBJ_H
