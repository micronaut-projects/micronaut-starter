$version = '2.1.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '81E9A7B0C194005EFA602354E04BBF607C2D99AA064A4FEAB002AD3540F73A8C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
