#ifndef QTMYOBJ_H
#define QTMYOBJ_H

#include <QObject>
#include <QDebug>

class QtMyObj : public QObject
{

    Q_OBJECT
    Q_PROPERTY(Priority priority READ priority WRITE setPriority NOTIFY priorityChanged)
    Q_ENUMS(Priority)

public:
    explicit QtMyObj(QObject *parent = 0) {
        Q_UNUSED(parent)
        qDebug() << "QtMyObj created";
    };

    enum Priority { High=0, Low, VeryHigh, VeryLow };

    void setPriority(Priority priority)
    {
        m_priority = priority;
        emit priorityChanged(priority);
    }

    Priority priority() const
    { return m_priority; }

signals:
    void priorityChanged(Priority);

private:
    Priority m_priority;

};

#endif // QTMYOBJ_H
