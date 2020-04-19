# Learngine API

Search for streaming sources for movies based on a title. Also provides alternative titles in the corresponding languages.

## Development

Local selenium:

```
docker run -d -p 4444:4444 --name selenium --shm-size=2g selenium/standalone-chrome:3.141.59-20200326
```
Or using a local chromedriver (Mac)
```
brew cask install/upgrade caskroom/versions/chromedriver-beta
```

### Docker

```
docker build -t firens/learngine .
```