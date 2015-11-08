# Dash [![Build Status](https://magnum.travis-ci.com/bionicrm/saso-dash.svg?token=fpiAqsfNZoYfyAxhver7&branch=master)](https://magnum.travis-ci.com/bionicrm/saso-dash)

A WebSocket server for Saso.

See [saso-web](https://github.com/bionicrm/saso-web) for the Ruby on Rails app.

### Requirements
- Java JDK 8 or better
- Maven 3

### Setup

- Make a hosts entry to forward `ws.saso.dev` to `127.0.0.1`
- Run the WebSocket server like you would any other Java app, or optionally run the Maven CLI: `clean package exec:java`
