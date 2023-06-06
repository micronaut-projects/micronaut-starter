$version = '3.9.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '392F1FCAE1BEC494562288B7046D5A2F503C1FCE57A4387FCDA422AFF9C4BAF2'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
