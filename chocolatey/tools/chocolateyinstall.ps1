$version = '3.8.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '3F77655D53FE4787234CD8404154C947F94623DB753E4501271788081C7ECDD9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
