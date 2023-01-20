$version = '3.8.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '30FA645D329B97C40F2F03D17A669956E133FD4B691848402DD1B0F7FF8352C8'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
