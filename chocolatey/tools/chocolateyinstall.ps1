$version = '3.6.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'DF700F5EFCC78B4B86A28ED2634E0853B6E9434CE896454E19A0FA1438848104'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
