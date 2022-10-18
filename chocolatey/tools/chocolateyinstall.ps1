$version = '3.7.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8A99BB1081896BD25BAA6CA0A8015657D390DB359F18979A1980178FD7AFE656'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
