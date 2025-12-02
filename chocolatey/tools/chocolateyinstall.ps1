$version = '4.10.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '131DCBC6DEB46027A10A8A326EA1CFCB6DCB08D7C03E177C0431D99C97BC03DD'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
