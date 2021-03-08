$version = '2.3.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '195BD91B16A15E894504CEFFA5B1DD6E0D53BD0ED46C3A256761F40A9389167B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
