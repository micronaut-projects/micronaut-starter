$version = '4.7.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '9F0B744D73908D6714CAA404544C84F7DFD55B2E26A0CB08F1C44548A1AD5EDD'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
