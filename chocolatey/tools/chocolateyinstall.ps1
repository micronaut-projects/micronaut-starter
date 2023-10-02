$version = '3.8.10'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '9FE2F7C949874FFC56C22FDC4721C45BDEB90A983989C27800DB4D58EF1793E9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
