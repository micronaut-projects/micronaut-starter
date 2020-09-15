$version = '2.0.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '67CE2F83B9382D2B21909C0FB98AE97FF3EA4C5F88613125A8AA13044260CABB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
