FROM nginx
COPY nginx.conf /etc/nginx/nginx.conf

COPY 3245791_home.ecoeler.com.key /etc/nginx/3245791_home.ecoeler.com.key
COPY 3245791_home.ecoeler.com.pem /etc/nginx/3245791_home.ecoeler.com.pem

COPY 3359587_oauth.ecoeler.com.key /etc/nginx/3359587_oauth.ecoeler.com.key
COPY 3359587_oauth.ecoeler.com.pem /etc/nginx/3359587_oauth.ecoeler.com.pem

COPY 3430740_files.ecoeler.com.key /etc/nginx/3430740_files.ecoeler.com.key
COPY 3430740_files.ecoeler.com.pem /etc/nginx/3430740_files.ecoeler.com.pem

COPY 4633170_g.ecoeler.com.key /etc/nginx/4633170_g.ecoeler.com.key
COPY 4633170_g.ecoeler.com.pem /etc/nginx/4633170_g.ecoeler.com.pem

RUN ["mkdir","-p","/nginx/files"]
RUN ["mkdir","-p","/nginx/html"]
RUN ["mkdir","-p","/nginx/logs"]

