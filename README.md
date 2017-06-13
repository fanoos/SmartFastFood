# SmartFastFood

<img src="http://i64.tinypic.com/25r16hu.jpg" width="900" height="450"/>
<br>

Sapienza - Università di Roma 
Master of Science in Engineering in Computer Science 
Pervasive Systems, a.y. 2016-17 
Group project realized by Federico Palmaro, Giuseppe D'Alpino, Giordano Orchi for Pervasive Systems class from MS in Computer Engineering at Sapienza - Università di Roma.

# Description

Are you tired of waiting for a long queue when you are in a fast food? <br/>
Would you like them to be even faster? Quite simple! Try Smart FastFood! <br/>
We propose an application that allows you to order food without doing a queue, without keeping cash or credit cards and reduce total cost of fast food. <br/>
The approch is: there is an application that uses the GPS to find participating fast foods in the nearby and send to user a notification on smartphone. In fast food there is a QR Code on each table. You can scan it with our application which shows to user menù and a form to make an order in a few steps. When user completes his order, it can simple pay with paypal account and after payment the order will be processed. The application will nofity you when order is ready.

<b>Customers benefits</b>:
<ul>
<li>You don't need to stand up for a long time in queue</li>
<li>You don't need to keep any type of money</li>
</ul>

<b>Fast Foods owner benefits</b>:
<ul>
<li>It reduces occupation time of the table by the customers</li>
<li>It reduce cost to assume too many employees</li>
<li>It makes the restaurant smart and cool</li>
</ul>

# Main functionalities

The main functionalities offered by the application are:<br/>
<b>Customer side</b>
<ul>
<li>Scan Qr Code or find the nearest fast food to receive menus</li>
<li>Order a menu through your smartphone</li>
<li>Pay through Paypal or money and take your order</li>
</ul>

<b>Employee side</b>
<ul>
<li>Look at orders to process and check if the customer has already paid or not</li>
</ul>

<b>Owner side</b>
<ul>
<li>Look at orders to process and check if the customer has already paid or not</li>
<li>Look to the different menus proposed</li>
<li>Add or delete a menu</li>
</ul>

# Technologies

<b>Android</b>:
- Minimum API 17
- Target API 25
- Compiled with API 25

<img src="http://i65.tinypic.com/2h32982.png" width="150" height="200"/>

<b>QR code scan</b>:<br/>
- Linked with the menus of the fast food
<img src="http://i68.tinypic.com/2nv5fmo.png" width="150" height="150"/>

<b>Paypal payments</b>:<br/>
- Paypal APIs
<img src="http://i65.tinypic.com/35imvyc.png" width="200" height="150"/>

<b>Gps receivers</b>:<br/>
- Google APIs
<img src="http://i68.tinypic.com/21k08w9.png" width="200" height="150"/>



# Architecture

<img src="http://i63.tinypic.com/2nv5lw0.png"/>

Our front-end is made up of an <b>Android</b> operating system application. <br/>
The applicationa allows the log-in of various users. From the latter you can choose the menus and then pay them. Payment is made through the <b>Paypal API</b>. All is handled by the <b>c9.io</b> back-end with the <b>laravel</b> framework with the language <b>php</b> . The DBMS that saves all transactions and data is <b>MySql</b> .

# Screenshots

# Members of this project

<ul>

<li> <a href="https://www.linkedin.com/in/federico-palmaro-634092116/">Federico Palmaro</a></li>
<li> <a href="https://www.linkedin.com/in/giuseppe-d-alpino-517561134/">Giuseppe D'Alpino</a></li>
<li> <a href="https://www.linkedin.com/in/giordano-orchi-245789133/">Giordano Orchi</a></li>

</ul>

# Others Info

<ul>
<li><a href="https://www.slideshare.net/FedericoPalmaro/smart-fastfood-73538257">Slides of fast presentation</a></li>
<li><a href="https://www.slideshare.net/FedericoPalmaro/smart-fast-food">Slides of intermediate presentation</a></li>
<li><a href="https://www.slideshare.net/FedericoPalmaro/smart-fast-food-75445849">Slides of 3rd presentation (MVP)</a></li>
<li><a href="https://www.slideshare.net/FedericoPalmaro/smarta-fastfood">Slides of final presentation</a></li>
</ul>

Informations about the course are available in the following page: http://ichatz.me/index.php/Site/PervasiveSystems2017

