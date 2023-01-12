$version = '3.8.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C59F9AC02FC6BD6DD8008E0BA7F3F10955FAEFF46743DF79F74D0F6C3FA2C329'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
