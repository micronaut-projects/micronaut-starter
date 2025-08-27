$version = '4.9.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'E90F4C099CDD5E9A1B9C22F1F810F48BBA9760782AC0C7D250AD1BEBF493B685'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
