$version = '4.1.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '34EFA8D1B04EB8163578224527D79BFC3F643D67C41187A24FE9A8A8772D6FD4'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
