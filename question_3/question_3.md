# Three Node VM - Install three servers using Xen / KVM hypervisor. Use two of them to install any application server (Apache etc) and any database server (MySQL etc). Create an application (say a simple login) on the third using the other two servers. Access the application from your host OS and another system.

## Network Setup
We use the bridge network created in previous question.

> Make sure to use the bridge instead of the default NAT when creating a VM

## Server specification
We use the same VMs as in [previous question](../question_2/question_2.pdf).

## Server Setup
We use the same setup as in [previous question](../question_2/question_2.pdf) for
the ```Web Server``` and ```Database Server```. Runner Node is not required in
this case.

## Brief of the deployed application
A web application was deployed using this setup. The application currently can
store records of campgrounds where each record consists of an image URL, body 
and a title. This data is persisted in a database. The server hosts a RESTful API
and a frontend which can be used to browse pre-existing records or add a new record.
The server listens on following routes:
```shell
# lists all the campgrounds currently in the database
GET/campgrounds
# adds a new campground from the given data
POST/campgrounds
# renders a form to create a new campground
GET/campgrounds/new
# gets the campground specified by id
GET/campgrounds/:id
```

## Testing

The deployed web application was tested by accessing it from the host OS as well
as from a mobile device connected to the same network. Two new records were
added apart from the initial seeding data.

![Web application running on the host and a mobile device](steps/collage.png)