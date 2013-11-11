#!/bin/sh

scp -i ~/Desktop/brianedmonds.pem ~/Desktop/Research-Jarek/WARPIC_3.0/web_app/processing.js ubuntu@ec2-54-237-34-141.compute-1.amazonaws.com:/var/www/processing.js

scp -i ~/Desktop/brianedmonds.pem ~/Desktop/Research-Jarek/WARPIC_3.0/web_app/index.html ubuntu@ec2-54-237-34-141.compute-1.amazonaws.com:/var/www/index.html

scp -i ~/Desktop/brianedmonds.pem ~/Desktop/Research-Jarek/WARPIC_3.0/web_app/WARPIC_WEB_M.pde ubuntu@ec2-54-237-34-141.compute-1.amazonaws.com:/var/www/WARPIC_WEB_M.pde
