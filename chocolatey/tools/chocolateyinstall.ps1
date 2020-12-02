$version = '2.2.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8303EBF8C451DBBE3881F3840F6ED1376CB544B6F512F9C00C6BE599B27DB6A6'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
