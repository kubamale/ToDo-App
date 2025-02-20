FROM node:18 AS build

# Set the working directory
WORKDIR /app

COPY . /app

RUN npm install -g @angular/cli
RUN npm install

CMD ["ng", "serve", "--host", "0.0.0.0", "--configuration=production"]
